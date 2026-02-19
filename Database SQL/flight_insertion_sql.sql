INSERT INTO Route (origin, destination, distance, status)
VALUES
    ('Vancouver', 'Toronto', 3358.00, 'Active'),       -- routeID = 6
    ('Edmonton', 'Calgary', 280.00, 'Active'),         -- routeID = 7
    ('Ottawa', 'Montreal', 150.00, 'Active'),          -- routeID = 8
    ('New York', 'Toronto', 560.00, 'Active'),         -- routeID = 9
    ('San Francisco', 'Vancouver', 1300.00, 'Active'); -- routeID = 10
    
INSERT INTO Flight (flightName, routeID, aircraftID, departureTime, arrivalTime, status)
VALUES
    -- Toronto -> Vancouver (routeID = 1)
    ('AC150', 1, 1, '2025-12-15 07:00:00', '2025-12-15 11:45:00', 'Scheduled'),
    ('WS151', 1, 2, '2025-12-18 13:30:00', '2025-12-18 18:15:00', 'Scheduled'),
    ('AC152', 1, 3, '2025-12-21 20:00:00', '2025-12-21 23:45:00', 'Scheduled'),

    -- Vancouver -> Toronto (routeID = 6, return of route 1)
    ('AC153', 6, 1, '2025-12-16 08:00:00', '2025-12-16 12:45:00', 'Scheduled'),
    ('WS154', 6, 2, '2025-12-19 14:15:00', '2025-12-19 19:00:00', 'Scheduled'),
    ('AC155', 6, 3, '2025-12-22 18:30:00', '2025-12-22 23:15:00', 'Scheduled'),

    -- Calgary -> Edmonton (routeID = 2)
    ('WS200', 2, 2, '2025-12-15 09:00:00', '2025-12-15 09:50:00', 'Scheduled'),
    ('WS201', 2, 4, '2025-12-17 16:30:00', '2025-12-17 17:20:00', 'Scheduled'),
    ('WS202', 2, 2, '2025-12-20 07:15:00', '2025-12-20 08:05:00', 'Scheduled'),

    -- Edmonton -> Calgary (routeID = 7, return of route 2)
    ('WS203', 7, 2, '2025-12-15 11:00:00', '2025-12-15 11:50:00', 'Scheduled'),
    ('WS204', 7, 4, '2025-12-18 08:45:00', '2025-12-18 09:35:00', 'Scheduled'),
    ('WS205', 7, 2, '2025-12-21 17:00:00', '2025-12-21 17:50:00', 'Scheduled'),

    -- Montreal -> Ottawa (routeID = 3)
    ('AC300', 3, 4, '2025-12-16 06:30:00', '2025-12-16 07:20:00', 'Scheduled'),
    ('AC301', 3, 4, '2025-12-19 12:00:00', '2025-12-19 12:50:00', 'Scheduled'),
    ('AC302', 3, 4, '2025-12-22 19:15:00', '2025-12-22 20:05:00', 'Scheduled'),

    -- Ottawa -> Montreal (routeID = 8, return of route 3)
    ('AC303', 8, 4, '2025-12-16 08:00:00', '2025-12-16 08:50:00', 'Scheduled'),
    ('AC304', 8, 4, '2025-12-20 10:30:00', '2025-12-20 11:20:00', 'Scheduled'),
    ('AC305', 8, 4, '2025-12-23 21:00:00', '2025-12-23 21:50:00', 'Scheduled'),

    -- Toronto -> New York (routeID = 4)
    ('DL400', 4, 3, '2025-12-17 07:15:00', '2025-12-17 09:00:00', 'Scheduled'),
    ('DL401', 4, 3, '2025-12-19 15:00:00', '2025-12-19 16:45:00', 'Scheduled'),
    ('DL402', 4, 3, '2025-12-22 20:30:00', '2025-12-22 22:15:00', 'Scheduled'),

    -- New York -> Toronto (routeID = 9, return of route 4)
    ('DL403', 9, 3, '2025-12-17 11:00:00', '2025-12-17 12:45:00', 'Scheduled'),
    ('DL404', 9, 3, '2025-12-20 09:30:00', '2025-12-20 11:15:00', 'Scheduled'),
    ('DL405', 9, 3, '2025-12-23 18:00:00', '2025-12-23 19:45:00', 'Scheduled'),

    -- Vancouver -> San Francisco (routeID = 5)
    ('UA500', 5, 1, '2025-12-18 06:45:00', '2025-12-18 09:30:00', 'Scheduled'),
    ('UA501', 5, 1, '2025-12-21 13:15:00', '2025-12-21 16:00:00', 'Scheduled'),
    ('UA502', 5, 1, '2025-12-24 19:00:00', '2025-12-24 21:45:00', 'Scheduled'),

    -- San Francisco -> Vancouver (routeID = 10, return of route 5)
    ('UA503', 10, 1, '2025-12-19 07:30:00', '2025-12-19 10:15:00', 'Scheduled'),
    ('UA504', 10, 1, '2025-12-22 14:45:00', '2025-12-22 17:30:00', 'Scheduled'),
    ('UA505', 10, 1, '2025-12-26 20:30:00', '2025-12-26 23:15:00', 'Scheduled'),
    
    -- Vancouver -> Toronto (routeID = 6, return of route 1)
    ('AC101R', 6, 1, '2025-12-10 14:00:00', '2025-12-10 17:45:00', 'Scheduled'),
    ('AC101R2', 6, 1, '2025-12-12 14:00:00', '2025-12-12 17:45:00', 'Scheduled');