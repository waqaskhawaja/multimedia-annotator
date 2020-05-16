import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MaTestModule } from '../../../test.module';
import { AnalysisScenarioDeleteDialogComponent } from 'app/entities/analysis-scenario/analysis-scenario-delete-dialog.component';
import { AnalysisScenarioService } from 'app/entities/analysis-scenario/analysis-scenario.service';

describe('Component Tests', () => {
    describe('AnalysisScenario Management Delete Component', () => {
        let comp: AnalysisScenarioDeleteDialogComponent;
        let fixture: ComponentFixture<AnalysisScenarioDeleteDialogComponent>;
        let service: AnalysisScenarioService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisScenarioDeleteDialogComponent]
            })
                .overrideTemplate(AnalysisScenarioDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisScenarioDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisScenarioService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
