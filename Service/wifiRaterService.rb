#!/usr/bin/ruby

require 'sinatra'
require 'json'
require 'mysql'

user='furby'
pass='furby'
host='localhost'
database = 'wifiRater'


con = Mysql.new host, user, pass, database



######################## Auth ##########################
use Rack::Auth::Basic, "Restricted Area" do |username, password|
  username == user and password == pass
end

################ Home (load Readme.txt) #######################
get '/' do
      page = File.read('README.txt')
      page.gsub(/\n/, "</br>")
end

########################### Ratings #############################

#send all the ratings
get '/Ratings/?' do

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
    rs = con.query("SELECT * FROM Ratings WHERE bssid='#{params[:bssid]}'")
    rs.fetch_hash().to_json()
end 

#Recieve new rating for bssid
get '/Ratings/:bssid/:rating/?' do
    
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


################### User hashes:#########################
get '/User/?' do
    "Nigga, please... especify userame pls";
end

#Send user hashed passwd
get '/User/:user/?' do
    rs = con.query("SELECT passwd FROM Users WHERE user='#{params[:user]}'")
    rs.fetch_row().to_json()
end
