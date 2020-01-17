import { Moment } from 'moment';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

export interface IAnnotation {
    id?: number;
    start?: Moment;
    end?: Moment;
    annotationText?: string;
    annotationSession?: IAnnotationSession;
    interactionRecords?: IInteractionRecord[];
}

export class Annotation implements IAnnotation {
    constructor(
        public id?: number,
        public start?: Moment,
        public end?: Moment,
        public annotationText?: string,
        public annotationSession?: IAnnotationSession,
        public interactionRecords?: IInteractionRecord[]
    ) {}
}
