import {Component, OnInit} from '@angular/core';
import {ArticleService} from 'src/app/services/article.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Article} from 'src/app/models/article.model';
import {ArticleTagModel} from '../../models/articleTag.model';
import {TokenStorageService} from '../../_services/token-storage.service';

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.css']
})
export class ArticleDetailsComponent implements OnInit {
  currentArticle: Article = {
    title: '',
    articleAbstract: ''
  };
  message = '';
  tagName = '';
  wikiURL = '';
  currentUser: any;

  constructor(
    private articleService: ArticleService,
    private route: ActivatedRoute,
    private router: Router,
    private token: TokenStorageService) {
  }

  ngOnInit(): void {
    this.message = '';
    this.tagName = '';
    this.wikiURL = '';
    this.currentUser = this.token.getUser();
    this.getArticle(this.route.snapshot.params.id);
  }

  getArticle(id: string): void {
    this.articleService.get(id)
      .subscribe(
        data => {
          this.currentArticle = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  tagArticle(): void {
    const postTag: ArticleTagModel = {
      tagName: this.tagName,
      wiki_url: this.wikiURL,
      pubmedArticleId: this.currentArticle.id,
      user_id: this.currentUser.id
    };
    this.articleService.tagArticle(postTag).subscribe(
      response => {
        console.log(response);
        this.message = response.message ? response.message : 'Article Tagged successfully';
        this.ngOnInit();
      },
      error => {
        console.log(error);
      });
  }

  updateArticle(): void {
    this.message = '';

    this.articleService.update(this.currentArticle.entityId, this.currentArticle)
      .subscribe(
        response => {
          console.log(response);
          this.message = response.message ? response.message : 'This article was updated successfully!';
        },
        error => {
          console.log(error);
        });
  }

  deleteArticle(): void {
    this.articleService.delete(this.currentArticle.entityId)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate(['/articles']);
        },
        error => {
          console.log(error);
        });
  }
}
