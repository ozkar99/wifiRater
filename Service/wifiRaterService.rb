#!/usr/bin/ruby

require 'sinatra'
require 'json'
require 'mysql'


get '/' do
	#todo el ratings table
end

get '/:bssid' do
    #se manda el bssid y rating 
	"#{params[:bssid]}"
end 
