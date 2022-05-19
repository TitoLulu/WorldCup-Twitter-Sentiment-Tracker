from sys import api_version
import tweepy
import configparser
import time

from kafka import KafkaConsumer, KafkaProducer
from datetime import datetime
from kafka.admin import KafkaAdminClient,NewTopic



config = configparser.ConfigParser()
config.read('twitter.cfg')
consumer_key = config["AUTH"]["twitter_api_key"]
consumer_secret=config["AUTH"]["twitter_api_secret"]
access_token=config["AUTH"]["twitter_api_access_token"]
access_token_secret=config["AUTH"]["twitter_api_access_secret"]
auth = tweepy.OAuth1UserHandler(
consumer_key, consumer_secret, access_token, access_token_secret
)

api = tweepy.API(auth)
producer = KafkaProducer(bootstrap_servers='localhost:9092')
topic_name = 'worldcup'




def normalize_timestamp(time):
    time_value = datetime.strptime(time, '%Y-%m-%d %H:%M:%S%z')
    return (time_value.strftime('%Y-%m-%d %H:%M%S'))


def get_twitter_data():
    res = api.search_tweets("QATAR WORLD CUP OR WORLD CUP 2022")
    # print(res)
    for i in res:
        record = ''
        record += str(i.user.id_str)
        record += ";"
        record += str(normalize_timestamp(str(i.created_at)))
        record += ";"
        record += str(i.user.followers_count)
        record += ";"
        record += str(i.user.location)
        record += ";"
        record += str(i.user.favourites_count)
        record += ";"
        record += str(i.retweet_count)
        record += ";"
        producer.flush()
        producer.send('worldcup',str.encode(record))
        producer.flush()

  
        

get_twitter_data()
def schedule_work(interval: int):
    counter = 1
    while True:
        get_twitter_data()
        print(f"Sending data to Kafka, #{counter}")
        # [print(msg) for msg in consumer]
        counter += 1 
        time.sleep(interval)

schedule_work(60 * 0.1)
