# Phase 3: Frontend Evolution (Stunning Web UI)

Now that our backend is clean, organized, and serving flight data via JSON, we are ready to build the **Visual Interface**. We will create a modern, high-performance Single Page Application (SPA) using **Vite + React**.

## User Review Required

> [!IMPORTANT]
> **Design Vibe**: I will implement a "Premium Dark Mode" aesthetic with Glassmorphism (blurred translucent backgrounds), vibrant indigo/violet accents, and smooth smooth transitions. 
> - **Are you okay with a modern Dark Mode design, or do you prefer a clean Light Mode?**

## Proposed Changes

### [Frontend Layer] (React + Vite)
We will initialize a new `frontend/` directory and build the following components:

#### [NEW] [App.jsx](file:///c:/Users/sijux/Desktop/FALL-2025/ENSF-480/Project/frontend/src/App.jsx)
The main shell with a floating navigation bar and dynamic background.

#### [NEW] [FlightCard.jsx](file:///c:/Users/sijux/Desktop/FALL-2025/ENSF-480/Project/frontend/src/components/FlightCard.jsx)
A premium card to display flight details (Origin, Destination, Price, Time) with hover animations.

#### [NEW] [FlightSearch.jsx](file:///c:/Users/sijux/Desktop/FALL-2025/ENSF-480/Project/frontend/src/components/FlightSearch.jsx)
A glassmorphic search bar to filter flights in real-time.

#### [NEW] [index.css](file:///c:/Users/sijux/Desktop/FALL-2025/ENSF-480/Project/frontend/src/index.css)
The core design system:
- **Colors**: Rich dark neutrals (`#0f172a`) with vibrant indigo (`6366f1`) and violet (`a855f7`) gradients.
- **Typography**: Inter / Outfit from Google Fonts.
- **Glassmorphism**: Custom utilities for `backdrop-filter: blur()`.

## Open Questions

> [!WARNING]
> 1. **Framework Choice**: I've planned to use **Vite + React** for speed and modern standards. Does this work for your ENSF-480 requirements, or do you have a specific framework restriction?
> 2. **Navigation**: Should we start with a single-page view of all flights, or do you want a Login page first? (I recommend starting with Flight Search to see the "WOW" factor immediately).

## Verification Plan
1. **Local Dev Server**: Run `npm run dev` and verify the UI loads at `http://localhost:5173`.
2. **API Connection**: Verify the frontend successfully fetches and displays flights from `http://localhost:8080/api/flights`.
