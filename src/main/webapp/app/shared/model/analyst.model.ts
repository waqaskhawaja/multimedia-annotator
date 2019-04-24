import { ISession } from 'app/shared/model/session.model';

export interface IAnalyst {
    id?: number;
    name?: string;
    experience?: number;
    sessions?: ISession[];
}

export class Analyst implements IAnalyst {
    constructor(public id?: number, public name?: string, public experience?: number, public sessions?: ISession[]) {}
}
