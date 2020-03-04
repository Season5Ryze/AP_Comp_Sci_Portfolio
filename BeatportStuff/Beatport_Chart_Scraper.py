#This project scrapes a Beatport Chart and adds all of its songs to a designated Spotify playlist for later listening
#By: Michael West, www.github.com/Season5Ryze
from bs4 import BeautifulSoup 
import requests
import spotipy
import spotipy.util as util
import requests

#Authentication variables and credential flow
cid ="<Something>" 
secret = "<Something>"
username = "<Your Username>"
playlist = "<Playlist>"
scope = 'playlist-modify-public user-library-read playlist-modify-private user-library-modify'
redirect_uri = 'http://localhost:8888/callback'
token = util.prompt_for_user_token(username=username, scope=scope, client_id=cid, client_secret=secret, redirect_uri=redirect_uri)
sp = spotipy.Spotify(token)

#Use Requests to retrieve our chart for parsing with BS4
page = requests.get('https://www.beatport.com/chart/sound-rush-top-10/604269')
soup = BeautifulSoup(page.content, 'html.parser')

#Scan all tracks and get their name, artist name, and remix name if appliclable
for item in soup.find_all("li", class_="bucket-item ec-item track"):
	artist_name = item['data-ec-d1']
	song_name = item['data-ec-name']
	remix = item.find(class_='buk-track-remixed').text
  
  #Remove the Original Mix tags, but keep other remix versions, this is to try and make the spotify search process more accurate
	if remix == 'Original Mix' or remix == ' Original Mix' or remix == 'Original Mix ' or remix == ' Original Mix ':
		query = 'artist:' + artist_name + ',' + ' track:' + song_name
	else:
		query = 'artist:' + artist_name + ',' + ' track:' + song_name + ' ' + remix   
    
  #Search Spotify using our created search query 
	print(query)
	results = sp.search(query, limit=1, type='track')
	print(results)
  
  #Add the resulting tracks to our list
	tracks = results['tracks']['items']
	track_ids_for_spotify = []
	track = results['tracks']['items']
  
  #Go through the tracks and add them to our playlist
	for track in tracks:
		if (track['id']):
			#print(track)
			song = [track['id']]
			sp.user_playlist_add_tracks(username, playlist_id=playlist, tracks=song)
