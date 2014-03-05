#!/usr/bin/ruby

require 'mysql'
require 'json'
require 'sinatra'

require File.expand_path '../wifiRaterService.rb', __FILE__

run WifiRaterService
