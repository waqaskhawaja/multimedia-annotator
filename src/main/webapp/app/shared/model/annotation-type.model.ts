export interface IAnnotationType {
    id?: number;
    name?: string;
}

export class AnnotationType implements IAnnotationType {
    constructor(public id?: number, public name?: string) {}
}
