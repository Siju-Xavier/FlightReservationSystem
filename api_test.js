// Simple API test script for the Flight Reservation System
// Run with: node api_test.js
// Requires node-fetch (included in recent Node versions as global fetch), otherwise install: npm install node-fetch@2

(async () => {
  const baseUrl = 'http://localhost:8080/api';
  const loginPayload = {
    username: 'testuser',
    password: 'password123'
  };
  try {
    // Login request
    const loginResp = await fetch(`${baseUrl}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginPayload)
    });
    const loginData = await loginResp.json();
    if (!loginResp.ok) {
      console.error('Login failed:', loginData);
      return;
    }
    console.log('✅ Login successful →', loginData);

    // Fetch flights (no auth token needed in this demo)
    const flightsResp = await fetch(`${baseUrl}/flights`);
    const flights = await flightsResp.json();
    console.log('🛫 Flights received (', flights.length, '):');
    console.dir(flights, { depth: 2, colors: true });
  } catch (err) {
    console.error('Error during API test:', err);
  }
})();
