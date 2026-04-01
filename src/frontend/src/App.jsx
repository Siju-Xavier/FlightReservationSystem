import { useState, useEffect } from 'react';
import SeatMap from './components/SeatMap';
import LoginModal from './components/LoginModal';
import './index.css'

function App() {
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [showLoginModal, setShowLoginModal] = useState(false);

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
        <div>
          <h1>Canada Airways</h1>
          <p>Premium Flight Reservations</p>
        </div>
        <div className="auth-section">
          {currentUser ? (
            <div className="user-profile">
              <span className="welcome-text">Welcome, {currentUser.username}</span>
              <button className="secondary-btn" onClick={() => setCurrentUser(null)}>Logout</button>
            </div>
          ) : (
            <button className="primary-btn" onClick={() => setShowLoginModal(true)}>Login</button>
          )}
        </div>
      </header>

      <main className='content'>
        <section className='hero-section'>
          <h2>Find your next Destination</h2>
          <div className='search-bar glass'>
            <input 
              type='text' 
              placeholder='Where to? (e.g. Toronto)' 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button className='primary-btn'>Search</button>
          </div>
        </section>
        <section className="flight-list">
          {loading ? (
            <p className="status-text">Loading flights...</p>
          ) : flights.length > 0 ? (
            <div className="grid">
               {flights
                .filter(flight => 
                  flight.destination.toLowerCase().includes(searchTerm.toLowerCase()) || 
                  flight.origin.toLowerCase().includes(searchTerm.toLowerCase())
                )
                .map(flight => (
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
                  <button className="book-btn" onClick={() => setSelectedFlight(flight)}>Select Seats</button>
                </div>
              ))}
            </div>
          ) : (
            <p className="status-text">No flights found. Make sure your Java server is running!</p>
          )}
        </section>

        {/* Seat Map Modal Overlay */}
        {selectedFlight && (
           <SeatMap 
             flight={selectedFlight} 
             onClose={() => setSelectedFlight(null)} 
           />
        )}

        {/* Login Modal Overlay */}
        {showLoginModal && (
           <LoginModal 
             onClose={() => setShowLoginModal(false)}
             onLoginSuccess={(user) => {
               setCurrentUser(user);
               setShowLoginModal(false);
             }}
           />
        )}
      </main>
    </div>
  )
}

export default App