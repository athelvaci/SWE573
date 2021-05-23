import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleListComponent } from './components/article-list/article-list.component';
import { ArticleDetailsComponent } from './components/article-details/article-details.component';
import { AddArticleComponent } from './components/add-article/add-article.component';

const routes: Routes = [
  { path: '', redirectTo: 'articles', pathMatch: 'full' },
  { path: 'articles', component: ArticleListComponent },
  { path: 'articles/:id', component: ArticleDetailsComponent },
  { path: 'add', component: AddArticleComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
