
// File: ./index.js

// import dependencies
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const Pusher = require('pusher');
const PushNotifications = require('@pusher/push-notifications-server');

// initialise express
const app = express();
const pusher = new Pusher(require('./config.js'));
const pushNotifications = new PushNotifications(require('./config.js'))

function handleStock(req, res, stock) {
    let loopCount = 0;
    
    let sendToPusher = setInterval(() => {
        loopCount++;
        const changePercent = randomIntFromInterval(-10, 10)
        const currentValue  = randomIntFromInterval(2000, 20000);        
        const stockName = (stock === 'amazon') ? 'Amazon' : 'Apple'
        const price = currentValue.toString()

        // Send to pusher
        pusher.trigger('stock-channel', stockName, {currentValue, changePercent})

        pushNotifications.publish(
            ['stocks'],{
            fcm: {
              notification: {
                title: stockName,
                body: `The new value for ${stockName} is: ${price}`
              }
            }
          }).then((publishResponse) => {
            console.log('Just published:', publishResponse.publishId);
          });

        if (loopCount === 5) {
          clearInterval(sendToPusher)
        }
    }, 2000);
    
    res.json({success: 200})
}

app.get('/stock/amazon', (req, res) => handleStock(req, res, 'amazon'));
app.get('/stock/apple', (req, res) => handleStock(req, res, 'apple'));

function randomIntFromInterval(min,max) {
    return Math.floor(Math.random()*(max-min+1)+min);
}

const port = 5000;

app.listen(port, () => console.log(`Server is running on port ${port}`));
