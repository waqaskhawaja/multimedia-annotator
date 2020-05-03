import { Moment } from 'moment';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';

export interface IDataSet {
    id?: number;
    title?: string;
    date?: Moment;
    type?: string;
    contents?: any;
    identifier?: string;
    annotationSession?: IAnnotationSession;
}

export class DataSet implements IDataSet {
    constructor(
        public id?: number,
        public title?: string,
        public date?: Moment,
        public type?: string,
        public contents?: any,
        public identifier?: string,
        public annotationSession?: IAnnotationSession
    ) {}
}
