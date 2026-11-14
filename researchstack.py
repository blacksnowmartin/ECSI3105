import numpy as np
import pandas as pd

def evaluate_alpha(prices: pd.Series, lookback: int = 20) -> dict:
    returns = prices.pct_change().fillna(0)
    
    # Z-score mean-reversion signal
    rolling_mean = prices.rolling(lookback).mean()
    rolling_std = prices.rolling(lookback).std()
    z_score = (prices - rolling_mean) / rolling_std
    
    # Position: Long when oversold (z < -1), Short when overbought (z > 1)
    signal = np.where(z_score < -1, 1, np.where(z_score > 1, -1, 0))
    strategy_returns = pd.Series(signal, index=prices.index).shift(1).fillna(0) * returns
    
    # Annualized metrics (252 trading days)
    ann_return = strategy_returns.mean() * 252
    ann_vol = strategy_returns.std() * np.sqrt(252)
    sharpe = round(ann_return / ann_vol, 3) if ann_vol > 0 else 0.0
    
    cum_returns = (1 + strategy_returns).cumprod()
    max_drawdown = round(((cum_returns / cum_returns.cummax()) - 1).min(), 4)
    
    return {
        "Annualized Return": f"{ann_return:.2%}",
        "Sharpe Ratio": sharpe,
        "Max Drawdown": f"{max_drawdown:.2%}"
    }

# Simulated price series test
np.random.seed(42)
mock_prices = pd.Series(100 + np.cumsum(np.random.normal(0, 1, 500)))
print(evaluate_alpha(mock_prices))
