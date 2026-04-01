import { useState, useEffect } from 'react';
import './index.css'

function App() {
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/flights')
      .then(response => response.json())
      .then(data => {
        setFlights(data);
        setLoading(false);
      })
      .catch(err => {
        console.error("Failed to fetch data: ", err);
        setLoading(false);
      });
  }, []);

  return (
    <div className='app-container'>
      <div className='bg-glow'>
      </div>
      <header className='glass main-header'>
        <h1>Canada Airways</h1>
        <p>Premium Flight Reservations</p>
      </header>

      <main className='content'>
        <section className='hero-section'>
          <h2>Find your next Destination</h2>
          <div className='search-bar glass'>
            <input type='text' placeholder='Where to' />
            <button className='primary-btn'>Search</button>
          </div>
        </section>
        <section className="flight-list">
          {loading ? (
            <p className="status-text">Loading flights...</p>
          ) : flights.length > 0 ? (
            <div className="grid">
              {flights.map(flight => (
                <div key={flight.flightNumber} className="flight-card glass">
                  <div className="card-header">
                    <span className="airline">{flight.airline?.airlineName || 'Airline'}</span>
                    <span className="price">${flight.basePrice}</span>
                  </div>
                  <div className="route">
                    <div className="point">
                      <h3>{flight.origin}</h3>
                      <p>Departure</p>
                    </div>
                    <div className="plane-icon">✈️</div>
                    <div className="point">
                      <h3>{flight.destination}</h3>
                      <p>Arrival</p>
                    </div>
                  </div>
                  <button className="book-btn">Select Seats</button>
                </div>
              ))}
            </div>
          ) : (
            <p className="status-text">No flights found. Make sure your Java server is running!</p>
          )}
        </section>

      </main>
    </div>
  )
}

export default App