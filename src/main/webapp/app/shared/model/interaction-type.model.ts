import { IDataRecord } from 'app/shared/model/data-record.model';

export interface IInteractionType {
    id?: number;
    name?: string;
    dataRecords?: IDataRecord[];
}

export class InteractionType implements IInteractionType {
    constructor(public id?: number, public name?: string, public dataRecords?: IDataRecord[]) {}
}
