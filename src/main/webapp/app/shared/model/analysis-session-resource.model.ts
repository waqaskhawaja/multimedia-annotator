import { IResourceType } from 'app/shared/model/resource-type.model';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

export interface IAnalysisSessionResource {
    id?: number;
    name?: string;
    resourceType?: IResourceType;
    analysisSession?: IAnalysisSession;
}

export class AnalysisSessionResource implements IAnalysisSessionResource {
    constructor(public id?: number, public name?: string, public resourceType?: IResourceType, public analysisSession?: IAnalysisSession) {}
}
