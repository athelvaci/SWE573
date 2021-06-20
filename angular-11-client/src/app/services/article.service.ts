import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Article} from '../models/article.model';

const baseUrl = 'http://localhost:8080/api/articles';
const taggedArticlesUrl = 'http://localhost:8080/api/tagged-articles';
const pubmedApi = 'http://localhost:8080/api/entrez/article';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Article[]> {
    return this.http.get<Article[]>(taggedArticlesUrl);
  }

  getAllTaggedArticlesOfAUser(userId: any): Observable<Article[]> {
    return this.http.get<Article[]>(`${taggedArticlesUrl}/${userId}`);
  }

  get(id: any): Observable<Article> {
    return this.http.get(`${pubmedApi}/${id}`);
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }

  findByTitle(title: any): Observable<Article[]> {
    return this.http.get<Article[]>(`${pubmedApi}?query=${title}`);
  }

  getTaggedArticles(): Observable<any> {
    return this.http.get<Article[]>(`${pubmedApi}/tagged`);
  }
}
