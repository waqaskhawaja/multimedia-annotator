import { IDataType } from 'app/shared/model/data-type.model';
import { IDataRecord } from 'app/shared/model/data-record.model';
import { ISession } from 'app/shared/model/session.model';

export interface IData {
    id?: number;
    name?: string;
    sourceFileContentType?: string;
    sourceFile?: any;
    dataType?: IDataType;
    dataRecords?: IDataRecord[];
    sessions?: ISession[];
}

export class Data implements IData {
    constructor(
        public id?: number,
        public name?: string,
        public sourceFileContentType?: string,
        public sourceFile?: any,
        public dataType?: IDataType,
        public dataRecords?: IDataRecord[],
        public sessions?: ISession[]
    ) {}
}
