import React from 'react';
import '../index.css';

function SeatMap({ flight, onClose }) {
  // Automatically generate 10 rows and 4 columns (A, B, C, D)
  const rows = Array.from({ length: 10 }, (_, i) => i + 1);
  const letters = ['A', 'B', 'C', 'D'];

  // Mock function to color code seats
  const getSeatClass = (seatString) => {
    // Rows 1 and 2 are Business Class
    if (seatString.includes('1') || seatString.includes('2')) return 'seat business';
    // Randomly assign some seats as "occupied" just for the UI demo!
    if (Math.random() > 0.7) return 'seat occupied';
    // The rest are standard Economy
    return 'seat economy';
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
                    return <div key={seatStr} className={getSeatClass(seatStr)} title={seatStr} onClick={(e) => {
                      if(!e.target.className.includes('occupied')) {
                        alert(`Selected Seat: ${seatStr}`);
                      }
                    }}>{letter}</div>
                  })}
                  <div className="aisle"></div>
                  {letters.slice(2, 4).map(letter => {
                    const seatStr = `${letter}${row}`;
                    return <div key={seatStr} className={getSeatClass(seatStr)} title={seatStr} onClick={(e) => {
                      if(!e.target.className.includes('occupied')) {
                        alert(`Selected Seat: ${seatStr}`);
                      }
                    }}>{letter}</div>
                  })}
                </div>
             </div>
          ))}
        </div>

        <button className="primary-btn checkout-btn" onClick={onClose}>Proceed to Checkout</button>
      </div>
    </div>
  );
}

export default SeatMap;
