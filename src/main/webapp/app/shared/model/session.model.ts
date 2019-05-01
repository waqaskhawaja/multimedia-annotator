import { IAnalyst } from 'app/shared/model/analyst.model';
import { IScenario } from 'app/shared/model/scenario.model';
import { IData } from 'app/shared/model/data.model';

export interface ISession {
    id?: number;
    analyst?: IAnalyst;
    scenario?: IScenario;
    data?: IData;
}

export class Session implements ISession {
    constructor(public id?: number, public analyst?: IAnalyst, public scenario?: IScenario, public data?: IData) {}
}
