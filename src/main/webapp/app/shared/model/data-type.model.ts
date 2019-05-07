import { ISession } from 'app/shared/model/session.model';

export interface IDataType {
    id?: number;
    name?: string;
    sessions?: ISession[];
}

export class DataType implements IDataType {
    constructor(public id?: number, public name?: string, public sessions?: ISession[]) {}
}
