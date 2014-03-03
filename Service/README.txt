Este servicio es una API REST.


Explicacion de llamadas:

/Ratings/ -> Te regresa una lista de pares, [{bssid, rating}, {bssid, rating}]

/Ratings/:bssid  -> Te regresa 1 solo par {bssid, rating}

/Ratings/:bssid/:rating -> Actualiza (o inserta si no existe) en la base de datos, luego redirige a /Ratings/:bssid
