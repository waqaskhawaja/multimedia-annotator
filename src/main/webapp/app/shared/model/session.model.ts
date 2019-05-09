import { IDataType } from 'app/shared/model/data-type.model';
import { IScenario } from 'app/shared/model/scenario.model';

export interface ISession {
    id?: number;
    name?: string;
    sourceFileContentType?: string;
    sourceFile?: any;
    url?: string;
    dataType?: IDataType;
    scenario?: IScenario;
}

export class Session implements ISession {
    constructor(
        public id?: number,
        public name?: string,
        public sourceFileContentType?: string,
        public sourceFile?: any,
        public url?: string,
        public dataType?: IDataType,
        public scenario?: IScenario
    ) {}
}
