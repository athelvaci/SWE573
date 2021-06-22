import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {ArticleService} from '../services/article.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  content?: string;
  currentUser: any;

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router) {
  }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    if (this.currentUser.id != null) {
      this.redirectToArticle();
    }

    this.userService.getPublicContent().subscribe(
      data => {
        this.content = data;
      },
      err => {
        this.content = JSON.parse(err.error).message;
      }
    );
  }

  redirectToArticle(): void {
    this.router.navigate(['/articles']);
  }
}
