import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { IAnnotation } from 'app/shared/model/annotation.model';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

export interface IInteractionRecordDto {
    id?: number;
    duration?: number;
    text?: string;
    sourceId?: string;
    time?: number;
    interactionType?: IInteractionType;
}
export class InteractionRecordDto implements IInteractionRecordDto {
    constructor(
        public id?: number,
        public duration?: number,
        public text?: string,
        public sourceId?: string,
        public time?: number,
        public interactionType?: IInteractionType
    ) {}
}
