import React, { useState } from 'react';
import '../index.css';

function SeatMap({ flight, onClose }) {
  // State for payment form
  const [paymentMethod, setPaymentMethod] = useState('debit');
  const [cardNumber, setCardNumber] = useState('');
  const [cardHolderName, setCardHolderName] = useState('');
  const [expiryDate, setExpiryDate] = useState('');
  const [cvv, setCvv] = useState('');

  const rows = Array.from({ length: 10 }, (_, i) => i + 1);
  const letters = ['A', 'B', 'C', 'D'];

  const getSeatClass = (seatString) => {
    if (seatString.includes('1') || seatString.includes('2')) return 'seat business';
    if (Math.random() > 0.7) return 'seat occupied';
    return 'seat economy';
  };

  const handleCheckout = async () => {
    const payload = {
      amount: flight.basePrice || 0,
      paymentMethod,
      cardNumber,
      cardHolderName,
      expiryDate,
      cvv,
    };
    try {
      const resp = await fetch('http://localhost:8080/api/checkout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!resp.ok) throw new Error('Checkout failed');
      const data = await resp.json();
      alert(data.message || 'Payment successful');
      // Reset form
      setPaymentMethod('debit');
      setCardNumber('');
      setCardHolderName('');
      setExpiryDate('');
      setCvv('');
      onClose();
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content glass">
        <button className="close-btn" onClick={onClose}>X</button>
        <h2>Seat Map: {flight.flightNumber}</h2>
        <p className="route-text">{flight.origin} ➔ {flight.destination}</p>
        <div className="seat-legend">
          <div className="legend-item"><div className="seat business"></div> Business</div>
          <div className="legend-item"><div className="seat economy"></div> Economy</div>
          <div className="legend-item"><div className="seat occupied"></div> Taken</div>
        </div>
        <div className="plane-fuselage">
          {rows.map(row => (
            <div key={row} className="seat-row">
              <span className="row-label">{row}</span>
              <div className="seats-group">
                {letters.slice(0, 2).map(letter => {
                  const seatStr = `${letter}${row}`;
                  return (
                    <div key={seatStr} className={getSeatClass(seatStr)} title={seatStr}
                      onClick={e => { if (!e.target.className.includes('occupied')) alert(`Selected Seat: ${seatStr}`); }}>
                      {letter}
                    </div>
                  );
                })}
                <div className="aisle"></div>
                {letters.slice(2, 4).map(letter => {
                  const seatStr = `${letter}${row}`;
                  return (
                    <div key={seatStr} className={getSeatClass(seatStr)} title={seatStr}
                      onClick={e => { if (!e.target.className.includes('occupied')) alert(`Selected Seat: ${seatStr}`); }}>
                      {letter}
                    </div>
                  );
                })}
              </div>
            </div>
          ))}
        </div>
        {/* Checkout Form */}
        <div className="checkout-form" style={{ marginTop: '1rem' }}>
          <label>
            Payment Method:
            <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)}>
              <option value="debit">Debit Card</option>
              <option value="credit">Credit Card</option>
            </select>
          </label>
          <br />
          <label>Card Number:
            <input type="text" value={cardNumber} onChange={e => setCardNumber(e.target.value)} />
          </label>
          <br />
          <label>Card Holder Name:
            <input type="text" value={cardHolderName} onChange={e => setCardHolderName(e.target.value)} />
          </label>
          <br />
          <label>Expiry Date (MM/YY):
            <input type="text" value={expiryDate} onChange={e => setExpiryDate(e.target.value)} />
          </label>
          <br />
          <label>CVV:
            <input type="text" value={cvv} onChange={e => setCvv(e.target.value)} />
          </label>
          <br />
          <button className="primary-btn checkout-btn" onClick={handleCheckout}>Proceed to Checkout</button>
        </div>
      </div>
    </div>
  );
}

export default SeatMap;
