from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import os

#Create custom HTTPRequestHandler class
class HTTPRequestHandler(BaseHTTPRequestHandler):
  
  #handle GET command
  def do_GET(self):
    rootdir = './' #file location
    
    try:
      str =''
      if(self.path == '/'):
        str = '/main.html'
      else :
        str = self.path
      
      f = open('.' + str) #open requested file

      #send code 200 response
      self.send_response(200)

      a = str.split('.')[-1]
      #send header first
      
      if(a == 'html'):
        self.send_header('Content-type','text/html')
      elif (a == 'css'):
        self.send_header('Content-type', 'text/css')
        
      elif (a == 'png'):
        self.send_header('Content-type', 'image/png')
        
      elif (a == 'jpg'):
        self.send_header('Content-type', 'image/jpg')
        
      elif (a == 'jpeg'):
        self.send_header('Content-type', 'image/jpeg')
        
      elif (a == 'js'):
        self.send_header('Content-type', 'application/javascript')

      elif (a == 'ttf'):
        self.send_header('Content-type', 'application/octet-stream')
      
      self.end_headers()

      #send file content to client
      self.wfile.write(f.read())
      f.close()
      return

    except IOError:
      self.send_error(404, 'file not found')
  
  def do_POST(self):
    username=request.form['username']
    password=request.form['password']
    print(username)
    print(password)
    (err,res) = find_grades(username,password)


def run():
  print('http server is starting...')

  #ip and port of servr
  #by default http server port is 80
  server_address = ('127.0.0.1', 80)
  httpd = HTTPServer(server_address, HTTPRequestHandler)
  print('http server is running...')
  httpd.serve_forever()
  
if __name__ == '__main__':
  run()