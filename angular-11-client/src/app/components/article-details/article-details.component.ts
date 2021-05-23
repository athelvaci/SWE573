import { Component, OnInit } from '@angular/core';
import { ArticleService } from 'src/app/services/article.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Article } from 'src/app/models/article.model';

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.css']
})
export class ArticleDetailsComponent implements OnInit {
  currentArticle: Article = {
    title: '',
    description: '',
    published: false
  };
  message = '';

  constructor(
    private articleService: ArticleService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.message = '';
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

  updatePublished(status: boolean): void {
    const data = {
      title: this.currentArticle.title,
      description: this.currentArticle.description,
      published: status
    };

    this.message = '';

    this.articleService.update(this.currentArticle.id, data)
      .subscribe(
        response => {
          this.currentArticle.published = status;
          console.log(response);
          this.message = response.message ? response.message : 'This article was updated successfully!';
        },
        error => {
          console.log(error);
        });
  }

  updateArticle(): void {
    this.message = '';

    this.articleService.update(this.currentArticle.id, this.currentArticle)
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
    this.articleService.delete(this.currentArticle.id)
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
