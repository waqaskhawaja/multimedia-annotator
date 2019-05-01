import { IDataType } from 'app/shared/model/data-type.model';
import { IScenario } from 'app/shared/model/scenario.model';
import { IDataRecord } from 'app/shared/model/data-record.model';

export interface ISession {
    id?: number;
    sourceFileContentType?: string;
    sourceFile?: any;
    dataType?: IDataType;
    scenario?: IScenario;
    dataRecords?: IDataRecord[];
}

export class Session implements ISession {
    constructor(
        public id?: number,
        public sourceFileContentType?: string,
        public sourceFile?: any,
        public dataType?: IDataType,
        public scenario?: IScenario,
        public dataRecords?: IDataRecord[]
    ) {}
}
