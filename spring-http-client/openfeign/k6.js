import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10,         // 동시에 10명
  iterations: 10,  // 총 10번만
};

export default function () {
  http.get('http://localhost:8080/fake');
}
