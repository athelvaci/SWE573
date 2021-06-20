import {Component, OnInit} from '@angular/core';
import {Article} from 'src/app/models/article.model';
import {ArticleService} from 'src/app/services/article.service';
import {TokenStorageService} from '../../_services/token-storage.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit {
  articles?: Article[];
  taggedArticles?: Article[];
  currentArticle?: Article;
  currentIndex = -1;
  title = '';
  currentUser: any;

  constructor(private articleService: ArticleService, private token: TokenStorageService) {
  }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.retrieveArticles();
  }

  retrieveArticles(): void {
    if (this.currentUser == null) {
      return;
    }

    this.articleService.getAll()
      .subscribe(
        data => {
          this.taggedArticles = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }


  setActiveArticle(article: Article, index: number): void {
    this.currentArticle = article;
    this.currentIndex = index;
  }

  searchTitle(): void {
    this.currentArticle = undefined;
    this.currentIndex = -1;

    this.articleService.findByTitle(this.title)
      .subscribe(
        data => {
          this.articles = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

}
