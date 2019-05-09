import { Moment } from 'moment';
import { ISession } from 'app/shared/model/session.model';
import { IUser } from 'app/core/user/user.model';

export interface IAnnotationSession {
    id?: number;
    start?: Moment;
    end?: Moment;
    name?: string;
    session?: ISession;
    annotator?: IUser;
}

export class AnnotationSession implements IAnnotationSession {
    constructor(
        public id?: number,
        public start?: Moment,
        public end?: Moment,
        public name?: string,
        public session?: ISession,
        public annotator?: IUser
    ) {}
}
