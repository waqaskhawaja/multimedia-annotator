import { IAnalysisSession } from 'app/shared/model/analysis-session.model';
import { IDataSet } from 'app/shared/model/data-set.model';

export interface IAnalysisScenario {
    id?: number;
    name?: string;
    analysisSessions?: IAnalysisSession[];
    dataSets?: IDataSet[];
}

export class AnalysisScenario implements IAnalysisScenario {
    constructor(public id?: number, public name?: string, public analysisSessions?: IAnalysisSession[], public dataSets?: IDataSet[]) {}
}
