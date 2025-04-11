/*
Write a query to find the total number of tracks sold (using the
Quantity field from invoice_items) for each album. Only include
albums that have sold more than 50 units. The query should
return the AlbumId and the total number of units sold for each
album, ordered by total number of tracks sold in descending
order.
 */

SELECT t.AlbumID,
       SUM(i.Quantity) AS totTracksSold
FROM invoice_items as i
INNER JOIN tracks as t ON i.TrackId = t.TrackId
GROUP BY t.AlbumID


/*

Write a query to list the average revenue generated from each
customer. The query should return the CustomerId, FirstName,
and LastName, along with the average revenue. Only include
customers who have  average $6.50 or more.
 */

SELECT c.customerID, c.firstName, c.LastName,
       ROUND(AVG(i.Total),2) AS AvgRev
FROM invoices AS i
INNER JOIN customers AS c ON i.CustomerId = c.CustomerId
GROUP BY c.customerID
HAVING AvgRev >= 6.50
ORDER BY AvgRev DESC;

/*
Write a query to find the number of tracks in Rock genre(s). The
query should return the Genre name(s) and the number of
tracks in each genre. Sort the results by the number of tracks in
descending order.

 */

SELECT COUNT(t.TrackId) AS totalTracks, g.Name AS GenreNames
FROM tracks as t
INNER JOIN genres as g on T.GenreId = g.GenreId
WHERE GenreNames LIKE '%Rock%'
GROUP BY g.GenreId
ORDER BY totalTracks DESC