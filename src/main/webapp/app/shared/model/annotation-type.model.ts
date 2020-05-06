import { IAnnotationSession } from 'app/shared/model/annotation-session.model';

export interface IAnnotationType {
    id?: number;
    name?: string;
    annotationSession?: IAnnotationSession;
}

export class AnnotationType implements IAnnotationType {
    constructor(public id?: number, public name?: string, public annotationSession?: IAnnotationSession) {}
}
