~~1) Add swagger~~

~~2) Add logging and registration~~

~~3) Create Trip~~

~~4) Get All Trips~~

~~5) Get Trip By Id~~

~~6) Update Trip~~

~~7) Delete Trip~~

~~8) Document repository~~

~~9) Document service - ability to add pictures.~~
~~10) Add security: password hashing + JWT auth~~

~~11) Add global exception handler with proper API error responses~~

12) Add DTO validation with @Valid and validation annotations

13) Restrict trips, documents, and packing items by authenticated user

~~14) Add ability to mark packing items as packed/unpacked~~

15) Add database migrations with Flyway or Liquibase

16) Move database credentials to environment variables and profiles

17) Add real document photo upload support

18) **HERE**: Add Itinerary CRUD for trips:
~~- create ItineraryItem request/response DTOs +~~
~~- create ItineraryItemRepository with findByTripId~~ 
~~- create ItineraryItemService with add, get by trip, edit, delete~~
~~- create ItineraryItemController endpoints~~
~~- validate required fields: title, startDateTime, tripId~~
~~- ensure itinerary items belong to the authenticated user's trip~~
~~- add tests for create, list by trip, update, and delete~~
