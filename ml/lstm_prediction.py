"""
Smart Earthen Buildings — Modèle LSTM de prédiction hygrothermique
Thèse Doctorat Génie Civil — Construction Durable et Intelligente

Objectif : Prédire température et humidité relative à 48h
Architecture : LSTM bidirectionnel avec attention
"""

import numpy as np
import pandas as pd
from tensorflow import keras
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_absolute_error, mean_squared_error
import matplotlib.pyplot as plt
import joblib
import json
from datetime import datetime, timedelta

# ===== CONFIGURATION =====
SEQUENCE_LENGTH = 48  # 48h d'historique en entrée
PREDICTION_HORIZON = 48  # 48h de prédiction
FEATURES = ['temperature', 'humidity', 'co2', 'pressure', 'outdoor_temp', 'outdoor_humidity']
TARGET = ['temperature', 'humidity']
BUILDING = 'PISE_A'


def load_data(filepath: str) -> pd.DataFrame:
    """Charge les données IoT depuis CSV/base de données."""
    df = pd.read_csv(filepath, parse_dates=['recorded_at'], index_col='recorded_at')
    df = df.resample('1H').mean().interpolate(method='time')
    print(f"✅ Données chargées : {len(df)} heures | {df.columns.tolist()}")
    return df


def prepare_sequences(df: pd.DataFrame, scaler: MinMaxScaler) -> tuple:
    """Prépare les séquences LSTM (X, y)."""
    data = scaler.fit_transform(df[FEATURES])

    X, y = [], []
    for i in range(SEQUENCE_LENGTH, len(data) - PREDICTION_HORIZON):
        X.append(data[i - SEQUENCE_LENGTH:i])
        y.append(data[i:i + PREDICTION_HORIZON, :2])  # Temp + Humidity

    return np.array(X), np.array(y)


def build_lstm_model(input_shape: tuple, output_steps: int) -> keras.Model:
    """
    Architecture LSTM bidirectionnel avec mécanisme d'attention.
    Optimal pour les séries temporelles hygrothermiques des bâtiments en terre.
    """
    inputs = keras.Input(shape=input_shape)

    # Couches LSTM bidirectionnelles
    x = keras.layers.Bidirectional(
        keras.layers.LSTM(128, return_sequences=True, dropout=0.2)
    )(inputs)
    x = keras.layers.Bidirectional(
        keras.layers.LSTM(64, return_sequences=True, dropout=0.2)
    )(x)

    # Mécanisme d'attention
    attention = keras.layers.Dense(1, activation='tanh')(x)
    attention = keras.layers.Flatten()(attention)
    attention = keras.layers.Activation('softmax')(attention)
    attention = keras.layers.RepeatVector(128)(attention)
    attention = keras.layers.Permute([2, 1])(attention)
    x = keras.layers.Multiply()([x, attention])
    x = keras.layers.Lambda(lambda a: keras.backend.sum(a, axis=1))(x)

    # Couches denses
    x = keras.layers.Dense(256, activation='relu')(x)
    x = keras.layers.Dropout(0.3)(x)
    x = keras.layers.Dense(128, activation='relu')(x)

    # Sortie : [output_steps, 2] (Temp + Humidity)
    outputs = keras.layers.Dense(output_steps * 2)(x)
    outputs = keras.layers.Reshape((output_steps, 2))(outputs)

    model = keras.Model(inputs, outputs)
    model.compile(
        optimizer=keras.optimizers.Adam(learning_rate=0.001),
        loss='huber',
        metrics=['mae']
    )
    return model


def train(df: pd.DataFrame, model_path: str = 'models/lstm_earthen.h5'):
    """Entraîne le modèle LSTM."""
    scaler = MinMaxScaler()
    X, y = prepare_sequences(df, scaler)

    # Split train/val/test (70/15/15)
    n = len(X)
    X_train, y_train = X[:int(0.7*n)], y[:int(0.7*n)]
    X_val, y_val = X[int(0.7*n):int(0.85*n)], y[int(0.7*n):int(0.85*n)]
    X_test, y_test = X[int(0.85*n):], y[int(0.85*n):]

    model = build_lstm_model((SEQUENCE_LENGTH, len(FEATURES)), PREDICTION_HORIZON)
    model.summary()

    callbacks = [
        keras.callbacks.EarlyStopping(monitor='val_loss', patience=15, restore_best_weights=True),
        keras.callbacks.ReduceLROnPlateau(monitor='val_loss', factor=0.5, patience=7),
        keras.callbacks.ModelCheckpoint(model_path, save_best_only=True)
    ]

    history = model.fit(
        X_train, y_train,
        validation_data=(X_val, y_val),
        epochs=200,
        batch_size=32,
        callbacks=callbacks,
        verbose=1
    )

    # Évaluation
    y_pred = model.predict(X_test)
    mae = mean_absolute_error(y_test.reshape(-1, 2), y_pred.reshape(-1, 2))
    rmse = np.sqrt(mean_squared_error(y_test.reshape(-1, 2), y_pred.reshape(-1, 2)))
    print(f"\n📊 Résultats Test — MAE: {mae:.3f} | RMSE: {rmse:.3f}")

    # Sauvegarder scaler
    joblib.dump(scaler, 'models/scaler_earthen.pkl')
    print(f"✅ Modèle sauvegardé : {model_path}")

    return model, scaler, history


def predict_48h(model, scaler, last_sequence: np.ndarray) -> dict:
    """Génère une prédiction sur 48h à partir des dernières 48h de données."""
    seq_scaled = scaler.transform(last_sequence)
    X = seq_scaled.reshape(1, SEQUENCE_LENGTH, len(FEATURES))

    pred_scaled = model.predict(X, verbose=0)[0]

    # Inverse transform (seulement les 2 premières colonnes : temp + humidity)
    dummy = np.zeros((PREDICTION_HORIZON, len(FEATURES)))
    dummy[:, :2] = pred_scaled
    pred = scaler.inverse_transform(dummy)[:, :2]

    timestamps = [
        (datetime.now() + timedelta(hours=i)).isoformat()
        for i in range(1, PREDICTION_HORIZON + 1)
    ]

    return {
        "model": "LSTM-Bidirectionnel-Attention",
        "building": BUILDING,
        "generated_at": datetime.now().isoformat(),
        "horizon_hours": PREDICTION_HORIZON,
        "predictions": [
            {
                "timestamp": timestamps[i],
                "temperature": round(float(pred[i, 0]), 2),
                "humidity": round(float(pred[i, 1]), 2),
                "confidence": round(0.95 - i * 0.003, 3)  # Confiance décroissante
            }
            for i in range(PREDICTION_HORIZON)
        ]
    }


if __name__ == '__main__':
    print("🏛️  Smart Earthen Buildings — Entraînement LSTM")
    print("=" * 50)

    # Exemple avec données simulées (remplacer par vraies données IoT)
    dates = pd.date_range(start='2024-01-01', periods=8760, freq='1H')
    df = pd.DataFrame({
        'recorded_at': dates,
        'temperature': 20 + 5 * np.sin(np.arange(8760) * 2 * np.pi / 24) + np.random.normal(0, 0.5, 8760),
        'humidity': 55 + 15 * np.cos(np.arange(8760) * 2 * np.pi / 24) + np.random.normal(0, 2, 8760),
        'co2': 450 + 200 * (np.random.rand(8760) > 0.8).astype(float),
        'pressure': 1013 + np.random.normal(0, 2, 8760),
        'outdoor_temp': 25 + 8 * np.sin(np.arange(8760) * 2 * np.pi / 24 - 0.5),
        'outdoor_humidity': 40 + 20 * np.random.rand(8760),
    }).set_index('recorded_at')

    model, scaler, history = train(df)
    print("\n🎯 Entraînement terminé avec succès!")
