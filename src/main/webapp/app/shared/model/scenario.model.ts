import { ISession } from 'app/shared/model/session.model';

export interface IScenario {
    id?: number;
    name?: string;
    sessions?: ISession[];
}

export class Scenario implements IScenario {
    constructor(public id?: number, public name?: string, public sessions?: ISession[]) {}
}
