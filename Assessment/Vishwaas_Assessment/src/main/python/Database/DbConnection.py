
import psycopg2


# Database connection details
dbname = 'vishwaas'
user = 'postgres'
password = '12345'
host = 'localhost'
port = '5432'


# Endpoint to read data from table
def get_connection():
    # Connect to database
    conn = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)

    # Create cursor
    return conn

