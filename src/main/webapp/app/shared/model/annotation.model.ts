import { Moment } from 'moment';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { IDataRecord } from 'app/shared/model/data-record.model';

export interface IAnnotation {
    id?: number;
    start?: Moment;
    end?: Moment;
    annotationText?: string;
    annotationSession?: IAnnotationSession;
    dataRecords?: IDataRecord[];
}

export class Annotation implements IAnnotation {
    constructor(
        public id?: number,
        public start?: Moment,
        public end?: Moment,
        public annotationText?: string,
        public annotationSession?: IAnnotationSession,
        public dataRecords?: IDataRecord[]
    ) {}
}
