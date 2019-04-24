import { IData } from 'app/shared/model/data.model';

export interface IDataType {
    id?: number;
    name?: string;
    data?: IData[];
}

export class DataType implements IDataType {
    constructor(public id?: number, public name?: string, public data?: IData[]) {}
}
