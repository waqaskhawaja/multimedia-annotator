import { IScenario } from 'app/shared/model/scenario.model';

export interface ISourceDataType {
    id?: number;
    name?: string;
    scenario?: IScenario;
}

export class SourceDataType implements ISourceDataType {
    constructor(public id?: number, public name?: string, public scenario?: IScenario) {}
}
