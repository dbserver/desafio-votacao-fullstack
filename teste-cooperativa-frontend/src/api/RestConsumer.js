import axios from 'axios';

export function createRestConsumer(baseURL, customHeaders = {}) {
  return axios.create({
    baseURL,
    headers: {
      'Content-Type': 'application/json',
      ...customHeaders
    }
  });
}

const RestConsumer = createRestConsumer('http://15.229.78.133:8081/');

export default RestConsumer;
