import { IAnnotationType } from 'app/shared/model/annotation-type.model';

export interface IAnnotationType {
    id?: number;
    name?: string;
    parent?: IAnnotationType;
}

export class AnnotationType implements IAnnotationType {
    constructor(public id?: number, public name?: string, public parent?: IAnnotationType) {}
}
