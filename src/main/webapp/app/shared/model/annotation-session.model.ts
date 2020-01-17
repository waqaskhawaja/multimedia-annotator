import { Moment } from 'moment';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';
import { IUser } from 'app/core/user/user.model';

export interface IAnnotationSession {
    id?: number;
    start?: Moment;
    end?: Moment;
    name?: string;
    analysisSession?: IAnalysisSession;
    annotator?: IUser;
}

export class AnnotationSession implements IAnnotationSession {
    constructor(
        public id?: number,
        public start?: Moment,
        public end?: Moment,
        public name?: string,
        public analysisSession?: IAnalysisSession,
        public annotator?: IUser
    ) {}
}
