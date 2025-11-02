from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello from Dockerized Jenkins Pipeline!'

if __name__ == '__main_':
    app.run(debug=True, host='0.0.0.0', port=5000)