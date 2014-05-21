#!/usr/bin/ruby

#require 'bcrypt'
require 'sinatra/base'
require 'json'
require 'mysql'

class WifiRaterService < Sinatra::Base

########## Database Variables ###################

user='furby'
pass='furby'
host='localhost'
database = 'wifiRater'

######################## Auth ##########################
use Rack::Auth::Basic, "Restricted Area" do |username, password|
  username == user and password == pass
end

################ Home #######################
get '/' do
    
"
<head>
<title>wifiRaterService</title>
</head>

<body>
  <h2>This is a RESTfull API Service.</h2>
  </br>

  <ul>
    <li>/Ratings/ -> returns a list of bssid,ratings pairs.  <b>[{bssid, rating}, {bssid, rating}]</b> </li>
    <li>/Ratings/:bssid  -> returns only specified bssid pair. <b>{bssid, rating}</b> </li>
    <li>/Ratings/:bssid/:rating -> updates or insert into the database the specified bssid,rating pair.</li>
  </ul>
  </br>

  Powered by <a href='http://www.openbsd.org/'> <img src='http://www.openbsd.org/art/puffy/puflogh200X50.gif' /> </a>
  
</body>
"

end

########################### Ratings #############################

#send all the ratings
get '/Ratings/?' do

	con = Mysql.new host, user, pass, database
	#dem whole ratings
    rs = con.query("SELECT * FROM Ratings")
    
    output = Array.new
    #iterate over sets of data
    rs.each_hash do |col|
        output << col
    end
    return output.to_json()

end

#Send bssid and rating pair
get '/Ratings/:bssid/?' do
	con = Mysql.new host, user, pass, database
    rs = con.query("SELECT * FROM Ratings WHERE bssid='#{params[:bssid]}'")
    rs.fetch_hash().to_json()
end 

#Recieve new rating for bssid
get '/Ratings/:bssid/:rating/?' do
    
	con = Mysql.new host, user, pass, database
    #check if record exist
    rs = con.query("SELECT * FROM Ratings WHERE bssid='#{params[:bssid]}'")
    
    if rs.fetch_row().nil?
        #if the bssid doesnt exist create it
        con.query("INSERT INTO Ratings (rating, bssid) VALUES (#{params[:rating]}, '#{params[:bssid]}')")     
   else
        #else update it
        con.query("UPDATE Ratings SET rating=#{params[:rating]} WHERE bssid='#{params[:bssid]}'")
   end
    
    #redirect to the get
    redirect to("/Ratings/#{params[:bssid]}")

end

=begin UserModule
################### User hashes:#########################
get '/User/?' do
    "Nigga please...</br>especify username";
end

#Send user hashed passwd
get '/User/:user/?' do
	con = Mysql.new host, user, pass, database
    rs = con.query("SELECT passwd FROM Users WHERE user='#{params[:user]}'")
    rs.fetch_row()
end

get '/User/:user/:pass/?' do
	 con = Mysql.new host, user, pass, database
   	 mypass = BCrypt::Password.create("#{params[:pass]}")

	 con.query( "INSERT INTO Users (user, passwd) VALUES ('#{params[:user]}', '" + mypass +"')" )

	redirect to("/User/#{params[:user]}")
end
=end

end #end class
