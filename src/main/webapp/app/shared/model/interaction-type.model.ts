import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

export interface IInteractionType {
    id?: number;
    name?: string;
    interactionRecords?: IInteractionRecord[];
}

export class InteractionType implements IInteractionType {
    constructor(public id?: number, public name?: string, public interactionRecords?: IInteractionRecord[]) {}
}
