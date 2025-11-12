from flask import Flask, request, g
import sqlite3

app = Flask(__name__)

def get_db():
    if 'db' not in g:
        g.db = sqlite3.connect('example.db')
    return g.db

@app.route('/user')
def get_user():
    username = request.args.get('username')
    db = get_db()
    # VULNERABILIDADE AQUI!
    cursor = db.execute(f"SELECT * FROM users WHERE username = '{username}'")
    user = cursor.fetchone()
    return str(user)