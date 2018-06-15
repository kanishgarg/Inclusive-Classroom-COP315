from flask import Flask, render_template,request, url_for, redirect
from flask import Markup
from flask_socketio import SocketIO, send, emit
from werkzeug.utils import secure_filename
#from scrape import find_grades
import mechanize
import cookielib
from bs4 import BeautifulSoup,NavigableString
import html2text
import string
import socket
import httplib
import ssl
import json

ACADEMICS_URL = 'https://academics1.iitd.ac.in/Academics/'


def validate(username,password):

	def connect(self):		#some code to deal with certificate validation
    		sock = socket.create_connection((self.host, self.port),
                                self.timeout, self.source_address)
    		if self._tunnel_host:
    			self.sock = sock
    			self._tunnel()

    		self.sock = ssl.wrap_socket(sock, self.key_file, self.cert_file, ssl_version=ssl.PROTOCOL_TLSv1)


	httplib.HTTPSConnection.connect = connect
	# Browser
	br = mechanize.Browser()

	# Browser options
	br.set_handle_equiv(True)
	br.set_handle_gzip(True)
	br.set_handle_redirect(True)
	br.set_handle_referer(True)
	br.set_handle_robots(False)
	br.set_handle_refresh(mechanize._http.HTTPRefreshProcessor(), max_time=1)

	br.addheaders = [('User-agent', 'Chrome')]

	# The site we will navigate into, handling it's session
    	r = br.open(ACADEMICS_URL)


	# Select the second (index one) form (the first form is a search query box)
	br.select_form(nr=0)

	# User credentials
	br.form['username'] = username
	br.form['password'] = password

	# Login
	br.submit()
	if (br.geturl() == 'https://academics1.iitd.ac.in/Academics/index.php?page=tryLogin'):
		return (True,"Invalid Login Credentials") 
	else:
		return (False,"Valid Login Credentials")

	def remove_attrs(soup):
	    for tag in soup.findAll(True):
	        tag.attrs = None
	    return soup

app = Flask(__name__,
            static_url_path='',
            static_folder='static',
            template_folder='templates')

app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

@app.route("/")
def main():
	return render_template('main.html')

@app.route("/slides")
def slides():
	return render_template('slides.html')

flag = False
@socketio.on('status')
def handle_status(auth_status, id):
	global flag
	jsonList = json.dumps(auth_status)
	jsondict = json.loads(jsonList)
	status = jsondict["status"]
	if status == True:
		flag = True

@app.route("/slides", methods=['POST'])
def upload_form():
	if request.method == 'POST':
		f = request.files['file-1[]']
		f.save('./uploads/' + secure_filename(f.filename))
		return render_template('thanks.html')

@app.route("/",methods=['POST'])
def main_form():
	if request.method == 'POST':
		username=request.form['username']
		password=request.form['password']
		(err,res) = validate(username,password)
		if(err):
			return render_template('main.html')
		else:
			socketio.emit('authorized_access')
			return redirect(url_for('slides'))


@app.route('/<path:path>')
def static_file(path):
    return app.send_static_file(path)

if __name__ == "__main__":
    socketio.run(app)
