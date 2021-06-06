import {Author} from './author.model';
import {ArticleTagModel} from './articleTag.model';

export class Article {
  id?: any;
  entityId?: any;
  title?: string;
  articleAbstract?: string;
  authors?: Author[];
  tags?: ArticleTagModel[];
  keywords?: string[];
  published?: boolean;
  rate?: number;
}
